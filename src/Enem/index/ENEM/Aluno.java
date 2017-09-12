package ENEM;

/**
 *
 * @author netoalves
 */
public class Aluno{
    private String nome;    // 60
    private long matricula;  // 8
    private short curso;    // 2
    private String email;   // 50
    private String rua;     // 80
    public static final int tamanho = 200;
    

    public Aluno() {
    }

    public Aluno(long matricula, String nome, String rua, String email,  short curso) {
        this.nome = nome;
        this.matricula = matricula;
        this.curso = curso;
        this.email = email;
        this.rua = rua;
    }

    

    public String getNome() {
        return nome;
    }

    public long getMatricula() {
        return matricula;
    }

    public short getCurso() {
        return curso;
    }

    public String getEmail() {
        return email;
    }

    public String getRua() {
        return rua;
    }
    
    @Override
    public String toString(){
        return "\tNome: "+this.getNome()+
                    "\n\tMatricula: "+this.getMatricula()+
                    "\n\trua: "+this.getRua()+
                    "\n\temail: "+this.getEmail()+
                    "\n\tcurso: "+this.getCurso();
           }
    
    

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setMatricula(long matricula) {
        this.matricula = matricula;
    }

    public void setCurso(short curso) {
        this.curso = curso;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }
}
