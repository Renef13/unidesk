INSERT INTO cursos (nome, campus)
VALUES ('Engenharia de Software', 'Campus Principal'),
       ('Ciência da Computação', 'Campus Secundário'),
       ('Análise e Desenvolvimento de Sistemas', 'Campus Principal'),
       ('Redes de Computadores', 'Campus Secundário');



INSERT INTO coordenacoes (nome, id_curso)
VALUES ('Coordenação de Engenharia de Software', 1),
       ('Coordenação de Ciência da Computação', 2),
       ('Coordenação de Análise e Desenvolvimento de Sistemas', 3),
       ('Coordenação de Redes de Computadores', 4);



INSERT INTO usuarios (nome, email, senha, role, usuario)
VALUES ('João Silva', 'joao.silva@example.com', 'senha123', 'USER', 'joao.silva'),
       ('Maria Souza', 'maria.souza@example.com', 'senha123', 'USER', 'maria.souza'),
       ('Usuario Teste', 'usuario.teste@discente.ufma.br', '$2a$10$HxeMIrPYXlJ6SaIE01bjQeikShHxgEFlavpqtUvyN6rnR/7LHe0mu', 'USER', 'usuario.teste'),
       ('Pedro Santos', 'pedro.santos@example.com', 'senha123', 'ADMIN', 'pedro.santos'),
       ('Ana Oliveira', 'ana.oliveira@example.com', 'senha123', 'ADMIN', 'ana.oliveira'),
       ('Carlos Pereira', 'carlos.pereira@example.com', 'senha123', 'ADMIN', 'carlos.pereira'),
       ('Mariana Costa', 'mariana.costa@example.com', '$2a$10$bAl2Mf20makrh5HtlugwOuzyp87LjcLXr8vJ0bZ.lf46531kcx8KS', 'ADMIN', 'usuario.admin');

--
--   "usuario": "usuario.teste",
--   "senha": "usuarioteste"
--   "usuario": "usuario.admin",
--   "senha": "senha123"



INSERT INTO alunos (id_usuario, matricula, id_curso)
VALUES (1, '20230001', 1),
       (2, '20230002', 2),
       (3,'99999999',2);



INSERT INTO coordenadores (id_usuario, ativo, matricula, id_coordenacao)
VALUES (4, TRUE, 'C001', 1),
       (5, TRUE, 'C002', 2);



INSERT INTO funcionarios_coordenacao (id_usuario, matricula, id_coordenacao)
VALUES (6, 'F001', 1),
       (7, 'F002', 2);



INSERT INTO categorias (nome)
VALUES ('Suporte Técnico'),
       ('Financeiro'),
       ('Acadêmico'),
       ('Infraestrutura');



INSERT INTO prioridades (nivel)
VALUES ('Baixa'),
       ('Média'),
       ('Alta'),
       ('Urgente');



INSERT INTO status (nome)
VALUES ('Aberto'),
       ('Em Andamento'),
       ('Fechado'),
       ('Pendente');



INSERT INTO tickets (titulo, descricao, data_criacao, data_atualizacao, id_coordenacao, id_func_coordenacao, id_aluno,
                     id_status, id_prioridade, id_categoria)
VALUES ('Problema com acesso ao sistema', 'Não consigo logar no sistema acadêmico.', NOW(), NOW(), 1, 6, 1, 1, 3, 1),
       ('Dúvida sobre nota', 'Minha nota de cálculo não aparece no histórico.', NOW(), NOW(), 2, 7, 2, 1, 2, 3),
       ('Solicitação de material', 'Preciso de acesso aos slides da aula de Banco de Dados.', NOW(), NOW(), 1, 6, 1, 1,
        1, 3),
       ('Erro no boleto', 'O valor do boleto de mensalidade está incorreto.', NOW(), NOW(), 2, 6, 2, 1, 4, 2);



