package aichat.springaigemini.model.brief;

import java.util.List;

public record BriefResponseRecord(

        String nome,
        String problemaPrincipal,
        List<String> publicoAlvo,
        List <String> featuresPrincipais,
        List <String> riscos,
        List <String> primeirosPassos



) {
}
