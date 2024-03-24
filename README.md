# spring-everytime-19th

CEOS 19th BE study - everytime clone coding

# Week 2

## DB 모델링

![image](https://github.com/CEOS-Developers/spring-everytime-19th/assets/116694226/9a5f7bdb-fc47-4253-a952-d818aad82133)

### 학교

<img src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/116694226/30e09e4a-cd85-448e-ac4a-2eda6a09be09" width=700>

- 학교마다 다른 게시판을 만들 수 있습니다.
- 회원은 학교에 소속되어야 합니다.

### 회원/메시지

<img src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/116694226/791c136f-c387-4d25-8fd7-e2be54301225" width=700>

- 회원은 여러 개의 메시지를 주고 받을 수 있습니다.

### 회원/댓글

<img src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/116694226/fe6f36f6-7847-4d39-a6e0-79ca20429c36" width=700>

- 회원은 댓글을 여러 개 달 수 있습니다.
- 회원은 댓글에 좋아요를 누를 수 있습니다.
- 회원은 대댓글을 달 수 있습니다.

### 게시글/댓글

<img src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/116694226/be705f67-b66c-44f7-a211-1ddce9eca8f6" height=500>

- 하나의 게시글에 여러 개의 댓글을 달 수 있습니다.

### 게시판/게시글

<img src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/116694226/0c63d084-f75f-4f53-8d04-13827cc536a1" width=700>

- 하나의 게시판에 여러 개의 게시글을 작성할 수 있습니다.

### 회원/게시글

<img src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/116694226/ce4986ee-eb39-47d3-bf93-b6a952bac63d" width=700>

- 회원은 여러 게시글에 좋아요를 누를 수 있습니다.
- 회원은 여러 게시글을 작성할 수 있습니다.

### 게시글/이미지

<img src="https://github.com/CEOS-Developers/spring-everytime-19th/assets/116694226/7d4211f6-e13a-40db-bd98-e440d6bbf9cc" width=700>

- 게시글에 여러 개의 이미지를 올릴 수 있습니다.

## Repository 단위 테스트

저는 `@DataJpaTest`를 사용해서 `PostRepository`에 대한 단위 테스트를 작성했습니다.

```java

@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    private User user;
    private Post post1;
    private Post post2;
    private Post post3;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("test")
                .password("qwe123!")
                .nickname("nick")
                .build();
        post1 = Post.builder()
                .title("title1")
                .user(user)
                .content("content1")
                .build();
        post2 = Post.builder()
                .title("title1")
                .user(user)
                .content("content1")
                .build();
        post3 = Post.builder()
                .title("title1")
                .user(user)
                .content("content1")
                .build();
        userRepository.save(user);
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
    }

    @Test
    void 게시글_조회_테스트() {
        // given & when
        final Post post1 = postRepository.findById(1L).get();
        final Post post2 = postRepository.findById(2L).get();
        final Post post3 = postRepository.findById(3L).get();

        // then
        assertAll(
                () -> assertThat(post1).usingRecursiveComparison().isEqualTo(this.post1),
                () -> assertThat(post2).usingRecursiveComparison().isEqualTo(this.post2),
                () -> assertThat(post3).usingRecursiveComparison().isEqualTo(this.post3)
        );
    }
}
```

하지만 테스트를 실행하면 다음과 같은 에러가 발생합니다.

```text
Caused by: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'dataSourceScriptDatabaseInitializer' defined in class path resource [org/springframework/boot/autoconfigure/sql/init/DataSourceInitializationConfiguration.class]: Unsatisfied dependency expressed through method 'dataSourceScriptDatabaseInitializer' parameter 0: Error creating bean with name 'dataSource': Failed to replace DataSource with an embedded database for tests. If you want an embedded database please put a supported one on the classpath or tune the replace attribute of @AutoConfigureTestDatabase.
	at org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorResolver.java:798)
	at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:542)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFactoryMethod(AbstractAutowireCapableBeanFactory.java:1335)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1165)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:562)
	...
```

`@DataJpaTest`는 기본적으로 내장 데이터베이스를 사용하기 때문에 `DataSource` 빈을 찾을 수 없다는 에러가 발생했습니다.
이 문제를 해결하기 위해 `@AutoConfigureTestDatabase`를 사용해서 기본 `DataSource` 빈을 교체하지 않는 설정을 했습니다.

```text
org.springframework.dao.DataIntegrityViolationException: not-null property references a null or transient value : com.ceos19.everytime.domain.User.createdAt
	at org.springframework.orm.jpa.vendor.HibernateJpaDialect.convertHibernateAccessException(HibernateJpaDialect.java:307)
	at org.springframework.orm.jpa.vendor.HibernateJpaDialect.translateExceptionIfPossible(HibernateJpaDialect.java:241)
	at org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.translateExceptionIfPossible(AbstractEntityManagerFactoryBean.java:550)
	at org.springframework.dao.support.ChainedPersistenceExceptionTranslator.translateExceptionIfPossible(ChainedPersistenceExceptionTranslator.java:61)
	at org.springframework.dao.support.DataAccessUtils.translateIfNecessary(DataAccessUtils.java:335)
	at org.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:152)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:184)
	at org.springframework.data.jpa.repository.support.CrudMethodMetadataPostProcessor$CrudMethodMetadataPopulatingMethodInterceptor.invoke(CrudMethodMetadataPostProcessor.java:164)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:184)
```

`@DataJpaTest`는 Repository와 관련된 빈만 등록하기 때문에 `@EnableJpaAuditing`이 있는 JpaAuditingConfig는 등록되지 않아 `@EnableJpaAuditing`이
적용되지 않았습니다.
이 문제를 해결하기 위해 `@Import`를 사용해서 `JpaAuditingConfig`를 등록했습니다.

```java

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaAuditingConfig.class)
class PostRepositoryTest {
    ...
}
```

이렇게 설정하고 테스트를 실행하면 테스트가 성공하고 다음과 같은 쿼리가 출력됩니다.

```sql
Hibernate: 
    insert 
    into
        users
        (created_at, nickname, password, school_id, updated_at, username) 
    values
        (?, ?, ?, ?, ?, ?)
Hibernate: 
    insert 
    into
        post
        (board_id, content, created_at, is_anonymous, title, updated_at, user_id) 
    values
        (?, ?, ?, ?, ?, ?, ?)
Hibernate: 
    insert 
    into
        post
        (board_id, content, created_at, is_anonymous, title, updated_at, user_id) 
    values
        (?, ?, ?, ?, ?, ?, ?)
Hibernate: 
    insert 
    into
        post
        (board_id, content, created_at, is_anonymous, title, updated_at, user_id) 
    values
        (?, ?, ?, ?, ?, ?, ?)
````

## 어떻게 data jpa는 interface만으로도 함수가 구현이 되는가?

이렇게 인터페이스로 구현할 수 있다는 것이 놀랍지 않은가요?

![image](https://github.com/CEOS-Developers/spring-everytime-19th/assets/116694226/145d4dcb-dfd9-498b-9534-e0e691eaa85f)

우리는 어떻게 구현체도 없이 findByName을 사용할 수 있을까요?

MemberService에서 memberRepository에 주입되는 빈을 살펴봅시다.
디버깅을 했더니 memberRepository에는 SimpleJpaRepository를 프록시로 주입하고 있었습니다.

![img](https://github.com/CEOS-Developers/spring-everytime-19th/assets/116694226/9418d160-3cca-4768-8657-0d4428c381f2))

Spring data jpa는 JpaRepositoryFactory에서 개발자가 정의한 MemberRepository를 참조하여 JpaRepository를 대신 구현해주는 역할을 합니다.

```java
public class JpaRepositoryFactory extends RepositoryFactorySupport {
    ...
}
```

RepositoryFactorySupport는 전달받은 Repository 인터페이스로 프록시 인스턴스를 생성하는 추상 클래스입니다.
Spring data XXX는 `RepositoryFactorySupport`를 확장해서 Repository를 생성합니다.
getRepository를 통해 Repository 인터페이스를 구현할 프록시를 생성합니다.

```java
public abstract class RepositoryFactorySupport implements BeanClassLoaderAware, BeanFactoryAware {
    ...

    // 첫 번째 파라미터 repositoryInterface로 MemberRepository가 전달됩니다.
    public <T> T getRepository(Class<T> repositoryInterface, RepositoryComposition.RepositoryFragments fragments) {
        ...

        // repositoryInterface로 전달된 MemberRepository의 인터페이스를 구현합니다.
        ProxyFactory result = new ProxyFactory();
        result.setTarget(target);
        result.setInterfaces(new Class[]{repositoryInterface, Repository.class, TransactionalProxy.class});
        
        ...

        T repository = result.getProxy(this.classLoader);
        
        ...
        
        // 프록시 인스턴스를 반환합니다.
        return repository;
    }
}
```

Spring data jpa에서는 `JpaRepositoryFactory`가 추상 메서드인 getRepositoryBaseClass가 `SimpleJpaRepository.class`를 반환하도록 오버라이드했습니다.

```java
protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
    return SimpleJpaRepository.class;
}
```

정리하자면 빈으로 생성되는 MemberRepository는 프록시 인스턴스이고 
JpaRepository는 프록시 인스턴스를 생성해 SimpleJpaRepsitory에 주입합니다.
그리고 프록시 인스턴스 내부에서 SimpleJpaRepsitory가 구현체를 실행합니다.

## References
- [https://brunch.co.kr/@anonymdevoo/40](https://brunch.co.kr/@anonymdevoo/40)
