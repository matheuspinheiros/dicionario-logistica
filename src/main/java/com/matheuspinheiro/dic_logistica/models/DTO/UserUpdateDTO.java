package com.matheuspinheiro.dic_logistica.models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserUpdateDTO {

    private Long id;

    @NotBlank
    @Size(min = 8, max = 50)
    private String password;
}
