package com.ceos19.everytime.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;



public record AddUserRequest (String email, String password){

}
