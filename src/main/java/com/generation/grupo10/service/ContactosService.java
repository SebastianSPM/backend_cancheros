package com.generation.grupo10.service;






import com.generation.grupo10.dto.ContactoRequest;
import com.generation.grupo10.dto.ContactoResponse;
import com.generation.grupo10.model.Contactos;
import com.generation.grupo10.repository.ContactosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactosService {

    private final ContactosRepository contactosRepository;

    public List<ContactoResponse> obtenerTodos() {

        return contactosRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public ContactoResponse guardarContacto(ContactoRequest request) {

        Contactos contacto = new Contactos();

        contacto.setNombre(request.getNombre());
        contacto.setEmail(request.getEmail());
        contacto.setTelefono(request.getTelefono());
        contacto.setMensaje(request.getMensaje());

        Contactos contactoGuardado = contactosRepository.save(contacto);

        return convertirAResponse(contactoGuardado);
    }

    private ContactoResponse convertirAResponse(Contactos contacto) {

        return new ContactoResponse(
                contacto.getId(),
                contacto.getNombre(),
                contacto.getEmail(),
                contacto.getTelefono(),
                contacto.getMensaje(),
                contacto.getFechaEnvio()
        );
    }
}