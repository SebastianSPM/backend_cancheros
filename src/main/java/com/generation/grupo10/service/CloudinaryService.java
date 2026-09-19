package com.generation.grupo10.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public ResultadoImagen subirImagen(
            MultipartFile archivo,
            String carpeta) {

        if (archivo == null || archivo.isEmpty()) {

            throw new IllegalArgumentException(
                    "No se recibió ninguna imagen."
            );
        }

        String contentType =
                archivo.getContentType();

        if (
                contentType == null ||
                        !contentType.startsWith("image/")
        ) {

            throw new IllegalArgumentException(
                    "El archivo debe ser una imagen."
            );
        }

        try {

            Map<?, ?> resultado =
                    cloudinary.uploader().upload(
                            archivo.getBytes(),
                            ObjectUtils.asMap(
                                    "folder",
                                    carpeta,
                                    "resource_type",
                                    "image"
                            )
                    );

            String url =
                    (String) resultado.get("secure_url");

            String publicId =
                    (String) resultado.get("public_id");

            return new ResultadoImagen(
                    url,
                    publicId
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "No se pudo subir la imagen a Cloudinary.",
                    e
            );
        }
    }

    public record ResultadoImagen(
            String url,
            String publicId
    ) {
    }
}