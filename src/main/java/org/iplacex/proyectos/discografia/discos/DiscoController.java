package org.iplacex.proyectos.discografia.discos;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class DiscoController {

    private final IDiscoRepository repo;

    public DiscoController(IDiscoRepository repo) {
        this.repo = repo;
    }

    // Crear disco
    @PostMapping(value = "/disco", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Disco> create(@RequestBody Disco disco) {
        Disco saved = repo.save(disco);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Listar discos
    @GetMapping(value = "/discos", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Disco> getAll() {
        return repo.findAll();
    }

    // Obtener disco por id
    @GetMapping(value = "/disco/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable String id) {
        return repo.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Disco no encontrado"));
    }

    // Actualizar disco
    @PutMapping(value = "/disco/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Disco body) {
        return repo.findById(id)
                .<ResponseEntity<?>>map(existing -> {
                    existing.setNombre(body.getNombre());
                    existing.setAnioLanzamiento(body.getAnioLanzamiento());
                    existing.setIdArtista(body.getIdArtista());
                    existing.setCanciones(body.getCanciones()); // Se agregó este método
                    return ResponseEntity.ok(repo.save(existing));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Disco no encontrado"));
    }

    // Eliminar disco
    @DeleteMapping("/disco/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return repo.findById(id)
                .<ResponseEntity<?>>map(d -> {
                    repo.deleteById(id);
                    return ResponseEntity.ok("Disco eliminado");
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Disco no encontrado"));
    }

    // Listar discos por ID de artista
    @GetMapping(value = "/artista/{artistaId}/discos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDiscosByArtista(@PathVariable String artistaId) {
        List<Disco> discos = repo.findByIdArtista(artistaId);
        if (discos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron discos para este artista");
        }
        return ResponseEntity.ok(discos);
    }
}