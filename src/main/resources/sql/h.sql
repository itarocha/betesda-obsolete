SELECT      q.id
          , q.numero
FROM        HospedeLeito hl
INNER JOIN  hl.quarto q
WHERE       (:DATA_INI BETWEEN hl.dataEntrada AND hl.dataSaida 
OR           :DATA_FIM BETWEEN hl.dataEntrada AND hl.dataSaida)
