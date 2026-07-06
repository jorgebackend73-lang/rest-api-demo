package com.example.utilities;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

// para que spring cree métodos al levantar el programa y autodetecte el componente.
@Component
public class FileUploadUtil {

    

    public String saveFile(String fileName, MultipartFile multipartFile) throws IOException {

        String fileCode = null; // variable a nivel local, inicializada a null.

        // Definimos ruta donde se guardará el archivo recibido o imagen.
        Path uploadPath = Paths.get ("Files-Upload");

        //Comprobamos si existe la ruta y la carpeta, si no, la creamos.
        if (!Files.exists(uploadPath))
            Files.createDirectories(uploadPath);

        // Generar el código alfanum de 8 caracteres y lo guardamos en variable a nivel de clase ^^
        // Generar el codigo alfanumerico de 8 caracteres

        // Genera un código que incluye números (0-9) y letras (a-z, A-Z)
        RandomStringGenerator generator = RandomStringGenerator.builder()
            .withinRange('0', 'z')
            .filteredBy(Character::isLetterOrDigit)
            .get();

        // Uso en tu código (ejemplo para 8 caracteres):
        fileCode = generator.generate(8);

        // NIO con rutas y cosas, además de con un try with resources
        // InputStream cuando se tabaja a nivel de bytes. .getInputStream devuelve array de bytes

        try (InputStream inputStream = multipartFile.getInputStream()) {
            // Se guarda en la ruta UploadPath de destino
            Path destino = uploadPath.resolve(fileCode + "-" + fileName); 
            //genera ruta destino
            Files.copy(inputStream, destino, StandardCopyOption.REPLACE_EXISTING); 
            //pegamos fichero en la ruta
            
        } catch (IOException ioe) {
            throw new IOException("Error guardando el archivo de imagen " + fileName, ioe);
        }

        return fileCode;
    }

}
