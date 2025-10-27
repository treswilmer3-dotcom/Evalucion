package hello.controller;

import hello.model.Persona;
import hello.repository.PersonaRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/personas")
public class PersonaController {

    private final PersonaRepository personaRepository;

    public PersonaController(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    @GetMapping
    public String listarPersonas(Model model) {
        model.addAttribute("personas", personaRepository.findAll());
        return "personas/list";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNuevaPersona(Model model) {
        model.addAttribute("persona", new Persona());
        return "personas/form";
    }

    @PostMapping("/guardar")
    public String guardarPersona(@Valid @ModelAttribute("persona") Persona persona, 
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "personas/form";
        }
        
        // Verificar si es una actualización o un registro nuevo
        if (persona.getId() == null) {
            // Es un registro nuevo, verificar si la cédula ya existe
            if (personaRepository.existsByCedula(persona.getCedula())) {
                result.rejectValue("cedula", "error.persona", "La cédula ya está registrada");
                return "personas/form";
            }
        } else {
            // Es una actualización, verificar si la cédula ha cambiado
            Persona personaExistente = personaRepository.findById(persona.getId())
                .orElseThrow(() -> new IllegalArgumentException("ID de persona inválido: " + persona.getId()));
                
            if (!personaExistente.getCedula().equals(persona.getCedula()) && 
                personaRepository.existsByCedula(persona.getCedula())) {
                result.rejectValue("cedula", "error.persona", "La cédula ya está registrada");
                return "personas/form";
            }
        }
        
        personaRepository.save(persona);
        return "redirect:/personas?exito";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Persona persona = personaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID de persona inválido: " + id));
        model.addAttribute("persona", persona);
        return "personas/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarPersona(@PathVariable Long id) {
        personaRepository.deleteById(id);
        return "redirect:/personas?eliminado";
    }
}
