package Ferramentas;

import ENEM.Aluno;
import java.nio.ByteBuffer;

/**
 *
 * @author netoalves
 */
public class Conversor {
    public static ByteBuffer toByteBuffer(Aluno a){
        ByteBuffer bb = ByteBuffer.allocate(a.tamanho);
        
        
        byte[] b_nome = new byte[70];
        byte[] b_email = new byte[50];
        byte[] b_rua = new byte[50];
        
        b_nome = a.getNome().getBytes();
        b_email = a.getEmail().getBytes();
        b_rua = a.getRua().getBytes();
        
        bb.putLong(a.getMatricula());
        // bb.position(8);
        /**************************/
        bb.put(a.getNome().getBytes());
        /**************************/
        bb.position(68);
        bb.put(a.getRua().getBytes());
        /**************************/
        bb.position(148);
        bb.put(a.getEmail().getBytes());
        /**************************/
        bb.position(198);
        bb.putShort(a.getCurso());
        /**************************/
        bb.flip();
        
        return bb;
    }
    
    public static Aluno toAluno(ByteBuffer bb){
        bb.clear();
        /*
            matricula, nome, rua, email, curso
        */
        Aluno a = new Aluno();
        short curso;
        long mat;
        String nome, rua, email;
        byte[] b_nome = new byte[60];
        byte[] b_rua = new byte[80];
        byte[] b_email = new byte[50];
        /**************************/
        mat = bb.getLong(0);
        // System.out.println(mat);
        bb.position(8);
        //nome = new String(bb.get(b_nome));
        bb.get(b_nome);
        bb.position(68);
        bb.get(b_rua);
        bb.position(148);
        bb.get(b_email);
        bb.position(198);
        curso = bb.getShort();
        
        nome = new String(b_nome);
        rua = new String(b_rua);
        email = new String(b_email);
        /**************************/
        a.setMatricula(mat);
        a.setNome(nome);
        a.setRua(rua);
        a.setEmail(email);
        a.setCurso(curso);
        
        return a;
    }
    
}
