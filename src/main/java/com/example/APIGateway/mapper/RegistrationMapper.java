package com.example.APIGateway.mapper;

import com.example.APIGateway.dto.AuthRequest;
import com.example.APIGateway.dto.RegistrationRequest;
import com.example.APIGateway.dto.UserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegistrationMapper {

    UserRequest toUserRequest(RegistrationRequest request);

    @Mapping(target = "userId", ignore = true)
    AuthRequest toAuthRequest(RegistrationRequest request);
}
