package org.iplacex.proyectos.discografia.artistas;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ArtistaController {

    private final IArtistaRepository repo;

    public ArtistaController(IArtistaRepository repo) {
        this.repo = repo;
    }

    // Crear artista
    @PostMapping(value = "/artista", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Artista> create(@RequestBody Artista artista) {
        Artista saved = repo.save(artista);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Listar artistas
    @GetMapping(value = "/artistas", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Artista> getAll() {
        return repo.findAll();
    }

    // Obtener artista por id
    @GetMapping(value = "/artista/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable String id) {
        return repo.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Artista no encontrado"));
    }

    // Actualizar artista
    @PutMapping(value = "/artista/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Artista body) {
        return repo.findById(id)
                .<ResponseEntity<?>>map(existing -> {
                    existing.setNombre(body.getNombre());
                    existing.setEstilos(body.getEstilos());
                    existing.setAnioFundacion(body.getAnioFundacion());
                    existing.setEstaActivo(body.isEstaActivo());
                    return ResponseEntity.ok(repo.save(existing)); // ResponseEntity<Artista>
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Artista no encontrado"));
    }

    // Eliminar artista
    @DeleteMapping("/artista/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return repo.findById(id)
                .<ResponseEntity<?>>map(a -> {
                    repo.deleteById(id);
                    return ResponseEntity.ok("Artista eliminado"); // ResponseEntity<String>
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Artista no encontrado"));
    }
}