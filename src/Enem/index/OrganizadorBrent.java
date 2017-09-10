package Enem.index;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class OrganizadorBrent implements IFileOrganizer {

    FileChannel channel;

    public OrganizadorBrent(String fileName) throws IOException {

        File file = new File(fileName);

        RandomAccessFile rf = new RandomAccessFile(file, "rw");

        channel = rf.getChannel();

    }

    @Override
    public void addAluno(Aluno p) {

    }

    @Override
    public Aluno getAluno(long matric) {
        return null;
    }

    @Override
    public Aluno delAluno(long matric) {
        return null;
    }

    @Override
    public void addReg(Aluno p){

    }
}