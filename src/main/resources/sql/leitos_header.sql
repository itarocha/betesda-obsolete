SELECT      new br.com.itarocha.betesda.model.hospedagem.LeitoHeader(l.id, l.numero, q.id, q.numero) 
FROM        Leito l
INNER JOIN  l.quarto q
ORDER BY    q.numero, l.numero

