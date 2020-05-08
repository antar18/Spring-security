package fr.univ.orleans.webservices.livedemosecurity.controller;
import fr.univ.orleans.webservices.livedemosecurity.modele.Message;
import fr.univ.orleans.webservices.livedemosecurity.modele.Utilisateur;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;


@RestController
@RequestMapping("/api")
public class MessageController {
    private static List<Message> messages = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong(1L);

    private static Map<String, Utilisateur> utilisateurs = new TreeMap<>();

    public static Map<String, Utilisateur> getUtilisateurs(){
        return utilisateurs;
    }

    static {
        Utilisateur fred = new Utilisateur("fred","fred",false);
        Utilisateur admin = new Utilisateur("admin","admin",true);
        utilisateurs.put(fred.getLogin(),fred);
        utilisateurs.put(admin.getLogin(),admin);
    }


    @PostMapping("messages")
    public ResponseEntity<Message> create(Principal principal, @RequestBody Message message){
        String login = principal.getName();
        Message message1 = new Message(counter.getAndIncrement(),login + " : " + message.getTexte());
        messages.add(message1);

        //Response's URI
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(message1.getId()).toUri();

        return ResponseEntity.created(location).body(message1);

    }

    @GetMapping("messages")
    public ResponseEntity<List<Message>> getAll(){
        return ResponseEntity.ok().body(messages);
    }

    @GetMapping("messages/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable("id") long id){
        Optional<Message> message = messages.stream()
                .filter(msg -> msg.getId() == id)
                .findAny();

        if(message.isPresent()){
            return ResponseEntity.ok().body(message.get());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("messages/{id}")
    public ResponseEntity deleteMessage(@PathVariable("id") long id){
        for(int index = 0; index<messages.size();index++){
           if (messages.get(index).getId() == id){
              messages.remove(index);
               return ResponseEntity.noContent().build();
           }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/utilisateurs")
    public ResponseEntity<Utilisateur> createUtilisateur(@RequestBody Utilisateur utilisateur){
        Predicate<String> isOk = s -> (s!=null) && (s.length() >= 2);
        if(!isOk.test(utilisateur.getLogin()) || !isOk.test(utilisateur.getPassword())){
            return ResponseEntity.badRequest().build();
        }

        if(utilisateurs.containsKey(utilisateur.getLogin())){
            return ResponseEntity.badRequest().build();
        }

        utilisateurs.put(utilisateur.getLogin(),utilisateur);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(utilisateur.getLogin()).toUri();

        return ResponseEntity.created(location).body(utilisateur);
    }

    @GetMapping("/utilisateurs/{login}")
    @PreAuthorize("#login == authentication.principal.username")
    public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable("login") String login){

       /* if(!principal.getName().equals(login)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }*/
        if(utilisateurs.containsKey(login)){
            return ResponseEntity.ok().body(utilisateurs.get(login));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

}
