package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.graphql.dto.InputMapper;
import de.bentrm.datacat.graphql.dto.LoginInput;
import de.bentrm.datacat.graphql.dto.ProfileUpdateInput;
import de.bentrm.datacat.graphql.dto.SignupInput;
import de.bentrm.datacat.service.AuthenticationService;
import de.bentrm.datacat.service.ProfileService;
import de.bentrm.datacat.service.dto.ProfileDto;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AuthDataFetcherProvider implements MutationDataFetcherProvider {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private InputMapper inputMapper;

    @Override
    public Map<String, DataFetcher> getMutationDataFetchers() {
        return Map.ofEntries(
                Map.entry("signup", signup()),
                Map.entry("login", login()),
                Map.entry("profile", profile()),
                Map.entry("updateProfile", updateProfile())
        );
    }

    private DataFetcher<String> signup() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            SignupInput dto = mapper.convertValue(input, SignupInput.class);
            return authenticationService.signup(dto);
        };
    }

    private DataFetcher<String> login() {
        return environment -> {
            Map<String, Object> input = environment.getArgument("input");
            LoginInput dto = mapper.convertValue(input, LoginInput.class);
            return authenticationService.login(dto.getUsername(), dto.getPassword());
        };
    }

    private DataFetcher<ProfileDto> profile() {
        return env -> profileService.getProfile();
    }

    private DataFetcher<ProfileDto> updateProfile() {
        return env -> {
            Map<String, Object> input = env.getArgument("input");
            ProfileUpdateInput dto = mapper.convertValue(input, ProfileUpdateInput.class);
            return profileService.updateAccount(inputMapper.toDto(dto));
        };
    }
}
