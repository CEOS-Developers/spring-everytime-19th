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
Hibernate
: 
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
