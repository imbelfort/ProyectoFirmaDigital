FROM eclipse-temurin:8-jdk-focal AS builder

WORKDIR /app

# Copiar librerías y código fuente
COPY lib/ lib/
COPY src/ src/

# Compilar el sistema Java
RUN mkdir -p build/classes && \
    javac -encoding UTF-8 -cp "lib/*" -d build/classes \
    src/Utils/*.java \
    src/Models/*.java \
    src/Database/*.java \
    src/DAO/*.java \
    src/Services/*.java \
    src/Interfaces/*.java \
    src/Comunication/*.java \
    src/proyectomailgrupo20sa/*.java

# Imagen de Ejecución (Runtime Liviano)
FROM eclipse-temurin:8-jre-focal

WORKDIR /app

COPY --from=builder /app/build/classes /app/build/classes
COPY --from=builder /app/lib /app/lib

# Variables de entorno con defaults institucionales
ENV DB_HOST=mail.tecnoweb.org.bo
ENV DB_PORT=5432
ENV MAIL_HOST=mail.tecnoweb.org.bo
ENV MAIL_SMTP_PORT=25
ENV MAIL_POP_PORT=110

# Ejecutar el Demonio de Correo y Monitor
CMD ["java", "-cp", "build/classes:lib/*", "proyectomailgrupo20sa.Principal"]
