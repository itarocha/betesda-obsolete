
WHERE       ((hospedeLeito.dataEntrada BETWEEN :DATA_INI AND :DATA_FIM) OR (hospedeLeito.dataSaida BETWEEN :DATA_INI AND :DATA_FIM))
OR          ((hospedeLeito.dataEntrada <= :DATA_INI) AND (hospedeLeito.dataSaida >= :DATA_FIM))





/* hospedes */
SELECT     h.hospedagem_id
         , h.pessoa_id
         , p.nome
         , h.tipo_hospede_id
         , th.descricao
FROM       hospede h
INNER JOIN tipo_hospede th
ON         th.id = h.tipo_hospede_id
INNER JOIN pessoa p
ON         p.id = h.pessoa_id
WHERE      EXISTS (
		   SELECT  1
		   FROM    hospedagem hpd
		   WHERE   (:DATA_INI BETWEEN hpd.data_entrada AND COALESCE(hpd.data_efetiva_saida, hpd.data_prevista_saida) 
	 	   OR       :DATA_FIM BETWEEN hpd.data_entrada AND COALESCE(hpd.data_efetiva_saida, hpd.data_prevista_saida))
		   AND     hpd.id = h.hospedagem_id
)
ORDER BY  h.hospedagem_id
        , h.pessoa_id
