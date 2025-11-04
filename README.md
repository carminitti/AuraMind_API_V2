# AuraMind API (Minimal Auth + DB)

Endpoints:
- `POST /api/auth/register` { email, password, displayName }
- `POST /api/auth/login`    { email, password }
- `GET  /api/users/me`      -> requer `Authorization: Bearer <token>`
- `GET  /actuator/health`   -> health check

## 1) Configurar ambiente
Defina as variáveis de ambiente (exemplos):

**Windows PowerShell**
```
$env:DB_URL="jdbc:sqlserver://SEU_HOST:1433;databaseName=SUA_DB;encrypt=true;trustServerCertificate=true"
$env:DB_USER="seu_usuario"
$env:DB_PASS="sua_senha"
$env:JWT_SECRET="um_segredo_bem_grande_de_32+_chars"
$env:CORS_ORIGINS="http://localhost:3000"
```

**Linux/macOS**
```
export DB_URL="jdbc:sqlserver://SEU_HOST:1433;databaseName=SUA_DB;encrypt=true;trustServerCertificate=true"
export DB_USER="seu_usuario"
export DB_PASS="sua_senha"
export JWT_SECRET="um_segredo_bem_grande_de_32+_chars"
export CORS_ORIGINS="http://localhost:3000"
```

## 2) Preparar o banco
Execute o SQL de `src/main/resources/db/migration/V1__create_users.sql` no seu SQL Server.

## 3) Rodar local
```
./mvnw spring-boot:run
# ou
mvn clean package && java -jar target/*.jar
```

Teste:
```
curl -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d '{"email":"t@ex.com","password":"123456","displayName":"Teste"}'
curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d '{"email":"t@ex.com","password":"123456"}'
curl http://localhost:8080/api/users/me -H "Authorization: Bearer <TOKEN>"
```

## 4) Rodar com Docker
```
docker build -t auramind-api:latest .
docker run --rm -p 8080:8080   -e DB_URL="jdbc:sqlserver://SEU_HOST:1433;databaseName=SUA_DB;encrypt=true;trustServerCertificate=true"   -e DB_USER="seu_usuario"   -e DB_PASS="sua_senha"   -e JWT_SECRET="um_segredo_bem_grande_de_32+_chars"   -e CORS_ORIGINS="http://localhost:3000"   auramind-api:latest
```

## 5) Garantir conversa front ↔ back
- Use HTTPS em produção (Reverse proxy Nginx/Cloud).  
- No Android (emulador), baseUrl `http://10.0.2.2:8080/`. Em device real na mesma rede, use o IP do PC.
- Envie `Authorization: Bearer <token>` após login.  
- Ajuste CORS para o domínio do seu front em produção.

## 6) Deploy (Render)
- Build: `./mvnw clean package`
- Start: `java -jar target/*.jar`
- Env vars: DB_URL, DB_USER, DB_PASS, JWT_SECRET, CORS_ORIGINS
- Health check: `GET /actuator/health`

Pronto para acoplar endpoints de negócio (mensagens/diário) sob autenticação.
