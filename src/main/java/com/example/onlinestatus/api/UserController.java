package com.example.onlinestatus.api;

import com.example.onlinestatus.model.User;
import com.example.onlinestatus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("api/v1")
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("user/register") // done
    public ResponseEntity<Map<String, Object>> registerUser(
            @RequestBody Map<String, Object> userMap
    ) throws ParseException {
        String name = (String) userMap.get("name");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = dateFormat.parse(String.valueOf(userMap.get("date_of_birth")));
        Date date_of_birth = new Date(date.getTime());
        Boolean active = (Boolean) userMap.get("active");

        Integer id;
        if (active != null) {
            id = userService.registerWithActive(name, date_of_birth, active);
        } else {
            id = userService.register(name, date_of_birth);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("user_id", id);
        map.put("created", true);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("user")
    public ResponseEntity<List<User>> getActiveUsers(
            @RequestParam("active") Boolean active
    ) {
        List<User> activeUsers = userService.getActiveUsers(active);
        return new ResponseEntity<>(activeUsers, HttpStatus.OK);
    }

    @PutMapping("user/{id}")
    public ResponseEntity<Map<String, Object>> activateProfile(
            @PathVariable("id") Integer id,
            @RequestParam("active") Boolean active
    ) {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", id);

        if (userService.setActive(id, active) != null)
            map.put("active", userService.setActive(id, active));
        else
            map.put("message", "User not found");

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("user/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteProfile(
            @PathVariable("id") Integer id
    ) {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", id);
        map.put("deleted", userService.deleteUser(id));

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}
