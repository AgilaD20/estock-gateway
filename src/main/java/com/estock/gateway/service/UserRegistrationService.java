package com.estock.gateway.service;



import javax.transaction.Transactional;

import com.estock.gateway.entity.CreateUserResponseModel;
import com.estock.gateway.entity.CreateusrRequestModel;
import com.estock.gateway.entity.Userentity;
import com.estock.gateway.repo.RoleRepository;
import com.estock.gateway.repo.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserRegistrationService{

    private final UserRepository userrepository;

    private final RoleRepository rolerepository;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    public UserRegistrationService(UserRepository userrepository, ModelMapper modelmapper, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userrepository = userrepository;
        this.modelMapper = modelmapper;
        this.passwordEncoder = passwordEncoder;
        this.rolerepository = roleRepository;
    }

    @Transactional
    public CreateUserResponseModel creatuser(CreateusrRequestModel request)
    {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Userentity user = modelMapper.map(request, Userentity.class);
        user.setEncryptedpassword(passwordEncoder.encode(request.getPassword()));
        Userentity  usr = userrepository.save(user);
        userrepository.updateRole(usr.getId(),rolerepository.findByRoleName("USER").getRoleId());
        return modelMapper.map(user, CreateUserResponseModel.class);

    }



}
