package aichat.springaigemini.model.brief;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClientRequestRecord(@NotBlank @Size(max = 2000) String input) {


}
