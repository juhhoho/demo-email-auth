package _2024.winter.demoemailauth.dto;

import lombok.Data;

@Data
public class CheckAuthEmailRequest {
    public String email;
    public String authNum;
}
