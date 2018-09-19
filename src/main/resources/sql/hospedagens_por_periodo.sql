SELECT      hl.quarto_id
          , q.numero quarto_numero
          , hl.leito_id
          , l.numero leito_numero
          , h.id hospedagem_id
          , h.id hospede_id  
          , h.destinacao_hospedagem_id
          , dh.descricao destinacao_hospedagem_descricao
          , h.tipo_utilizacao
          , h.data_entrada
          , h.data_prevista_saida
          , h.data_efetiva_saida
          , COALESCE(h.data_efetiva_saida, h.data_prevista_saida) data_saida
          , hpd.pessoa_id
          , hl.data_entrada data_entrada_leito
          , hl.data_saida data_saida_leito
FROM        hospedagem h
INNER JOIN  hospede hpd
ON          hpd.hospedagem_id = h.id
INNER JOIN  hospede_leito hl
ON          hl.hospede_id = hpd.id
INNER JOIN  quarto q
ON          q.id = hl.quarto_id
INNER JOIN  leito l
ON          l.id = hl.leito_id
INNER JOIN  destinacao_hospedagem dh
ON          dh.id = h.destinacao_hospedagem_id
WHERE       (:DATA_INI BETWEEN hl.data_entrada AND hl.data_saida 
OR           :DATA_FIM BETWEEN hl.data_entrada AND hl.data_saida)
ORDER BY    q.numero
          , l.numero
          
/*
https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.at-query
*/          