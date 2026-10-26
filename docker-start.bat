@echo off
echo ========================================================
echo   LEVANTANDO ENTORNO DOCKER (POSTGRES + GREENMAIL + APP)
echo   Sistema de Firma Digital del Vicerrectorado UAGRM
echo ========================================================
docker compose up -d --build
echo.
echo Estado de los servicios:
docker compose ps
echo.
echo Puede monitorear los logs del demonio con: docker compose logs -f app
