@echo off
echo Diagnosing Maven issues...

echo.
echo 1. Checking Maven version...
mvn -version

echo.
echo 2. Clearing Maven cache...
rmdir /s /q "%USERPROFILE%\.m2\repository\org\openjfx" 2>nul

echo.
echo 3. Updating Maven dependencies...
mvn dependency:purge-local-repository

echo.
echo 4. Clean compile...
mvn clean compile

echo.
echo 5. Running application...
mvn javafx:run

pause
