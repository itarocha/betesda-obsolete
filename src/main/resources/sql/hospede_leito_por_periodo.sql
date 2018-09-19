/* hospede_leito */
SELECT      h.hospedagem_id
          , h.id hospede_id  
          , h.pessoa_id
          , hl.data_entrada
          , hl.data_saida
          , hl.quarto_id
          , q.numero quarto_numero
          , hl.leito_id
          , l.numero leito_numero
FROM        hospede h
INNER JOIN  hospede_leito hl
ON          hl.hospede_id = h.id
INNER JOIN  quarto q
ON          q.id = hl.quarto_id
INNER JOIN  leito l
ON          l.id = hl.leito_id
WHERE EXISTS ( SELECT   1
				FROM    hospedagem hpd
				WHERE   (:DATA_INI BETWEEN hpd.data_entrada AND COALESCE(hpd.data_efetiva_saida, hpd.data_prevista_saida) 
				OR       :DATA_FIM BETWEEN hpd.data_entrada AND COALESCE(hpd.data_efetiva_saida, hpd.data_prevista_saida))
				AND     hpd.id = h.hospedagem_id
)
ORDER BY h.hospedagem_id
       , h.id
       , hl.data_entrada
