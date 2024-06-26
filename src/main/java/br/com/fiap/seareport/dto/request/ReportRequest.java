package br.com.fiap.seareport.dto.request;

import br.com.fiap.seareport.entity.Category;
import br.com.fiap.seareport.entity.Location;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record ReportRequest(
        @NotBlank(message = "Descricao é obrigatório!")
        String description,
        @NotNull
        Location location,
        @NotNull
        int category,
        @Positive
        @NotNull(message = "O ID do usuario é obrigatório!")
        Long userId
) {
}
