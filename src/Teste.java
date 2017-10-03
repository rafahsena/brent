
import ENEM.Aluno;
import Organizador.OrganizadorBrent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jocelino Neto
 */
public class Teste {
    public static void main(String[] args) throws FileNotFoundException, IOException {
            File arquivo = new File("selected.db");
            String permissao = "r";
            RandomAccessFile ramFile = new RandomAccessFile(arquivo, permissao);
            FileChannel canal = ramFile.getChannel();
            
            OrganizadorBrent org = new OrganizadorBrent("enem_brent.db");
            
            for (int i = 0; i < 1000; i++) {
                ByteBuffer bb = ByteBuffer.allocate(8);
                long matricula = bb.getLong();
                
                Aluno a = org.getAluno(matricula);
                System.out.println(a);
        }
            
    }
}
