import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

// ... resto da classe UsersController ...

@GetMapping("/me")
public MeResponse me(Authentication authentication) {
    if (authentication == null || authentication.getPrincipal() == null) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado");
    }

    Object principal = authentication.getPrincipal();
    User u = null;

    // Caso o principal seja UserDetails (padrão do Spring)
    if (principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
        String email = userDetails.getUsername();
        u = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado: " + email));
    }
    // Caso o JwtAuthFilter tenha colocado apenas o user id como principal (Long)
    else if (principal instanceof Long idLong) {
        u = userRepository.findById(idLong)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado: id=" + idLong));
    }
    // Caso o principal venha como String (pode ser id em string ou email)
    else if (principal instanceof String pStr) {
        // tenta interpretar como id
        try {
            Long idParsed = Long.parseLong(pStr);
            u = userRepository.findById(idParsed)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado: id=" + pStr));
        } catch (NumberFormatException e) {
            // não é id, assume que seja email
            u = userRepository.findByEmail(pStr)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado: " + pStr));
        }
    } else {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Principal do authentication inválido");
    }

    MeResponse resp = new MeResponse();
    resp.setId(u.getId());
    resp.setEmail(u.getEmail());
    resp.setDisplayName(u.getDisplayName());
    return resp;
}
