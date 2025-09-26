package vn.iotstar.dto;
import lombok.Data;

@Data
public class CreateUserInput {
    private String fullname;
    private String email;
    private String password;
    private String phone;
}
