SELECT   hpd.id
       , hpd.destinacao_hospedagem_id
       , hpd.tipo_utilizacao
       , hpd.data_entrada
       , hpd.data_prevista_saida
       , hpd.data_efetiva_saida
       , COALESCE(hpd.data_efetiva_saida, hpd.data_prevista_saida) data_saida
FROM     hospedagem hpd
WHERE   :DATA_INI BETWEEN hpd.data_entrada AND COALESCE(hpd.data_efetiva_saida, hpd.data_prevista_saida) 
OR      :DATA_FIM BETWEEN hpd.data_entrada AND COALESCE(hpd.data_efetiva_saida, hpd.data_prevista_saida) 
ORDER BY hpd.data_entrada
