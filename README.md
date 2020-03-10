# cooperative-back

Desafio da cooperativa

Documentação da API:

1) Adicionar uma pauta:
chamar https://cooperative-sd.herokuapp.com/cooperative/agenda/add com um JSON, por exemplo: 
{
"name": "Nome da pauta"
}

2) Retornar todas as pautas:
chamar https://cooperative-sd.herokuapp.com/cooperative/agenda 

3) Abrir uma votação de uma pauta:
chamar https://cooperative-sd.herokuapp.com/cooperative/agenda/{id}/voting/session/open/{time}
substituindo {id} e {time} pelo id da pauta e pelo tempo, em minutos, que a votação deve ficar aberta, por exemplo:
https://cooperative-sd.herokuapp.com/cooperative/agenda/1/voting/session/open/10

4) Votar em uma pauta:
chamar https://cooperative-sd.herokuapp.com/cooperative/agenda/{id}/voting com um JSON, por exemplo: 
{
"vote": "Sim",
"associate": 1
}
Sendo o id do associado no campo associate e também substituir o {id} pelo id da pauta.

5) Obter resultado de uma votação de uma pauta
chamar https://cooperative-sd.herokuapp.com/cooperative/agenda/{id}/voting/result
Substituir o {id} pelo id da pauta.


A API foi hospedada em:
https://cooperative-sd.herokuapp.com/cooperative/

É possível acessar a interface em:
https://cooperative-sd.netlify.com/
