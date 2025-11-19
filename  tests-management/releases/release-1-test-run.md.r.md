# Test Run – Release 1
TMS-ID: RUN-001

## Objetivo
Executar testes unitários e validar cobertura global da aplicação Battleship.

---

## Resultados do Checklist de Operações

### Operações Gerais
- ✔️ Gerar relatórios de cobertura de código (Coverage Report) – CL-001
- ✔️ Executar todos os testes unitários automaticamente – CL-002
- ✔️ Validar níveis de cobertura (Class, Method, Line, Branch) – CL-003
- ✔️ Atualizar relatórios na diretoria /reports – CL-004

### Operações de Gestão de Testes
- ✔️ Criar e atualizar Test Suites no TMS – CL-005
- ✔️ Organizar casos de teste na diretoria test-cases – CL-006
- ✔️ Ligar ficheiros de teste a código com referências TMS (quando aplicável) – CL-007

---

## Resultados dos Test Cases

### Testes Unitários Principais
- ✔️ TC-001 – Construção de barcos (Barge, Caravel, Carrack, Frigate, Galleon)
- ✔️ TC-002 – Comportamento geral de Ship (stillFloating, occupies, tooCloseTo, boundaries)
- ✔️ TC-003 – Classe Position
- ✔️ TC-004 – Classe Compass
- ✔️ TC-005 – Classe Fleet
- ✔️ TC-006 – Comportamento de disparo (shoot)
- ✔️ TC-007 – Proximidade entre barcos
- ✔️ TC-008 – Limites do tabuleiro

### Testes Relacionados com Tasks (não influenciam cobertura)
- ⏺️ TC-009 – Validação de input – *não executado*
- ⏺️ TC-010 – Criação de frotas via Tasks – *não executado*
- ⏺️ TC-011 – Integração mínima com Terminal – *não executado*

---

## Cobertura Global
- **Class Coverage:** ~95–100%
- **Method Coverage:** ~95–100%
- **Line Coverage:** ~90–100%
- **Branch Coverage:** ~85–95%

> Nota: A cobertura exata varia conforme os ficheiros de cada membro.

---

## Conclusão
Todos os testes unitários principais foram executados com sucesso.  
A aplicação apresenta uma cobertura global elevada e estável, cumprindo os objetivos da entrega.

---
## Execução @LEI122720

- Estado dos testes unitários: **passed** (98/98 testes)
- Nenhum teste **failed**
- Relatório detalhado: [unit-tests-LEI122720.html](../../reports/tests/unit-tests-LEI122720.html)

Responsável pela execução: @LEI122720

---
### Tags
#test-run #coverage #release #battleship #unit-tests
