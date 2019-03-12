package com.difinite.oauth.controller;

import com.difinite.oauth.model.PasswordResetToken;
import com.difinite.oauth.model.UserPassword;
import com.difinite.oauth.model.UserProfile;
import com.difinite.oauth.model.UserRoles;
import com.difinite.oauth.repos.*;
import com.difinite.oauth.repos.*;
import com.google.gson.Gson;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.*;

/**
 * Created by Richardo Kusuma on 2/18/2019.
 */
@RestController
@EnableResourceServer
public class AuthenticatedUserController extends ResourceServerConfigurerAdapter{
    @Autowired
    UserProfileRepo userProfileRepo;
    @Autowired
    UserRolesRepo userRolesRepo;
    @Autowired
    RolesMenuRepo rolesMenuRepo;
    @Autowired
    RolesRepo rolesRepo;
    @Autowired
    UserPasswordRepo userPasswordRepo;
    @Autowired
    PasswordResetTokenRepo resetTokenRepo;
    @Autowired
    BCryptPasswordEncoder encoder;
    @Autowired
    RestTemplate restTemplate;

    //set what needed to secure and not
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(
                    "/user/password/reset**", 
                    "/user/signup**", 
                    "/request/reset-password**",
                    "/password/check/token**"
                ).permitAll()
                .anyRequest().authenticated();
    }

    @RequestMapping("/request/reset-password")
    public ResponseMessage requestResetPassword(@RequestParam("email") String email){
        if(userProfileRepo.amountBasedEmail(email) > 0){
            String url = "http://localhost:8002/send";
            //seting headers for post request
            HttpHeaders headers = new HttpHeaders();
            //generating random string for token
            UUID resetPasswordToken = UUID.randomUUID();
            //setting body for post request
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("to", email);
            map.add("subject", "Resetting Password");
            map.add("body", "<div style=\"background-color: lightgrey\">\n" +
                    "    <div style=\"background-color: darkslateblue; color: white; padding: 20px\">\n" +
                    "        <h1>Articlizr</h1>\n" +
                    "        <h3>Forgot Your Password</h3>\n" +
                    "    </div><br>\n" +
                    "    <div style=\"color: black; padding: 20px;\" >\n" +
                    "        For reset your password got link below. Dont forget not to show or telling somebody about this page for security reasons.\n" +
                    "        <br><br>\n" +
                    "        <a style=\"text-decoration: none; background-color: dimgrey; border-radius: 2px;\n" +
                    "        color: white; margin: 80px 0px; padding: 12px; max-width: 120px; text-align: center\"\n" +
                    "           href=\"http:localhost:3000/reset/password/"+resetPasswordToken.toString()+"\">Reset Password</a>\n" +
                    "        <br>\n" +
                    "    </div>\n" +
                    "</div>");
            //saving reset token to database
            PasswordResetToken resetTokenToSave = new PasswordResetToken();
            resetTokenToSave.email = email;
            resetTokenToSave.token = resetPasswordToken.toString();
            resetTokenToSave.expiryDate = new Date(System.currentTimeMillis() + 1000);
            resetTokenRepo.save(resetTokenToSave);
            //adding request for post method
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            String responseString = restTemplate.postForObject(url, request, String.class);

            //Give the token to client
            ResponseMessage ResponseMessage = new ResponseMessage("Your Request Have Been Send ", "Please check your email");
            return ResponseMessage;
        }
        return new ResponseMessage("Failed to Reset", "Your email not signed yet");
    }

    //for userInfo
    @RequestMapping("/user/me")
    @ResponseBody
    public Principal getPrincipal(Principal principal){
        return principal;
    }

    @RequestMapping("/user/profile")
    @ResponseBody
    public UserProfile getAuthenticatedUserProfile(Principal principal){
        UserProfile profileToDisplay = userProfileRepo.findByUsername(principal.getName());
        return profileToDisplay;
    }

    @RequestMapping("/user/roles")
    @ResponseBody
    public List<String> getAuthenticatedUserRoles(Principal principal){
        UserProfile profileToFind = userProfileRepo.findByUsername(principal.getName());
        List<UserRoles> usrRolesFetched = userRolesRepo.findByUsrprofile(profileToFind);
        List<String> roles = new ArrayList<String>();
        for(UserRoles usrRoles : usrRolesFetched){
            roles.add(usrRoles.roles.role);
        }
        return roles;
    }

    @RequestMapping("/user/rolesMenu")
    @ResponseBody
    public List getAuthenticatedUserRolesMenu(Principal principal){
        UserProfile userProfileToFind = userProfileRepo.findByUsername(principal.getName());
        String usrProfileId = userProfileToFind.id;
        return rolesMenuRepo.getRolesMenu(usrProfileId);
    }

    @RequestMapping("/user/signup")
    public ResponseMessage signUp(@RequestParam("username") String username, @RequestParam("password") String password,
                       @RequestParam("email") String email, @RequestParam("rolesId") int rolesId){
        //check if the data is exist or not
        int userAmountBasedEmail = userProfileRepo.amountBasedEmail(email);
        int userAmountBasedUsername = userProfileRepo.amountBasedUsername(username);
        if(userAmountBasedEmail > 0 && userAmountBasedUsername > 0){
            return new ResponseMessage("Register Failed", "This credentials already exists");
        }

        String usrIdToSave = String.valueOf(System.currentTimeMillis());
        usrIdToSave = usrIdToSave.substring(0, 4);
        usrIdToSave = "USR"+usrIdToSave;

        //saving user profile
        UserProfile userProfileToSave = new UserProfile();
        userProfileToSave.id = usrIdToSave;
        userProfileToSave.email = email;
        userProfileToSave.username = username;
        userProfileRepo.save(userProfileToSave);

        //saving user password
        Long userPasswordId = Long.valueOf(userPasswordRepo.findAll().size()+1);
        UserPassword userPasswordToSave = new UserPassword();
        userPasswordToSave.id = userPasswordId;
        userPasswordToSave.userProfile = userProfileToSave;
        userPasswordToSave.password = encoder.encode(password);
        userPasswordRepo.save(userPasswordToSave);

        //saving user roles
        ArrayList<UserRoles> tempUserRoles = new ArrayList<>();
        Iterator rolesIterator = userRolesRepo.findAll().iterator();
        while(rolesIterator.hasNext()){
            tempUserRoles.add((UserRoles) rolesIterator.next());
        }
        Long userRolesId = Long.valueOf(tempUserRoles.size()+1);
        UserRoles userRolesToSave = new UserRoles();
        userRolesToSave.id = userRolesId;
        userRolesToSave.roles = rolesRepo.findById((long) rolesId).get();
        userRolesToSave.usrprofile = userProfileToSave;
        userRolesRepo.save(userRolesToSave);

        return new ResponseMessage("Register Success", "Please go to signin page");
    }


    //Reset the password
    @RequestMapping("/user/password/reset")
    public ResponseMessage resetUserPassword(@RequestParam("password") String password, @RequestParam("reset_token") String resetToken){
        PasswordResetToken resetPassFetched = new PasswordResetToken();
        //Check that token is exist or not
        if(resetTokenRepo.findByToken(resetToken) != null){
            resetPassFetched = resetTokenRepo.findByToken(resetToken);
        }else{
            return new ResponseMessage("Token is Not Valid", "The token that used to reset password is not valid");
        }
        //check the reset token is expired or not
        if(resetPassFetched.expiryDate.getTime() > System.currentTimeMillis()){
            return new ResponseMessage("Token is Expired", "The token that used to reset password is expired");
        }
        UserProfile userProfileToFind = userProfileRepo.findByEmail(resetPassFetched.email);
        //check the three user password already used or not
        List<UserPassword> userPasswords = userPasswordRepo.findThreeByUserProfile(userProfileToFind.id);
        for(UserPassword userPassword : userPasswords){
            if(userPassword.password == password){
                ResponseMessage ResponseMessage = new ResponseMessage("Password are already used", "You cant use the password that has been used");
                return ResponseMessage;
            }
        }
        //save the new password
        Long userPasswordId = Long.valueOf(userPasswordRepo.findAll().size()+1);
        UserPassword userPasswordToSave = new UserPassword();
        userPasswordToSave.id = userPasswordId;
        userPasswordToSave.userProfile = userProfileToFind;
        userPasswordToSave.password = encoder.encode(password);
        userPasswordRepo.save(userPasswordToSave);
        return new ResponseMessage("Resest Success", "Your new password saved");
    }

    //check the password reset token
    @RequestMapping("/password/check/token")
    public boolean checkPasswordTokenValidity(@RequestParam("reset_token") String resetToken){
        int tokenAmountBasedToken =  resetTokenRepo.amountBasedToken(resetToken);
        if(tokenAmountBasedToken > 0) return true;
        return false;
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("rest-api");
    }

    class ResponseMessage{
        public String status;
        public String message;
        public ResponseMessage(String status, String message){this.message = message; this.status = status;}
    }

}
