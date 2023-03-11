// Name: Sreemoyee Mukherjee
// Course: Data Structures & Algorithms
// Assignment Number: 1

package edu.cmu.edu.sreemoym;

import edu.cmu.andrew.sreemoym.SinglyLinkedList;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class MerkleTree {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        SinglyLinkedList firstList = new SinglyLinkedList();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the file name: ");
        String filename = sc.nextLine();
        File file = new File(filename);
        Scanner fileReader = null;
        try {
            fileReader = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the file");
            e.printStackTrace();
        }
        while (fileReader.hasNextLine()) {
            String data = fileReader.nextLine();
            firstList.addAtEndNode(data);
        }
        findRoot(firstList);
        sc.close();
        // Merkle root of: smallFile.txt
        //A4E10610B30C40CA608058C521AD3D9EEC42C1892688903984580C56D3CF8A7D
        // Merkle root of: CrimeLatLonXY.csv -> THIS IS THE MERKLE ROOT THAT WE WERE SEEKING FOR!
        //A5A74A770E0C3922362202DAD62A97655F8652064CCCBE7D3EA2B588C7E07B58
        // Merkle root of: CrimeLatLonXY1990_Size2.csv
        //DDD49991D04273A7300EF24CFAD21E2706C145001483D161D53937D90F76C001
        // Merkle root of: CrimeLatLonXY1990_Size3.csv
        //313A2AD830ED85B5203C8C2A9895ADFA521CD4ABB74B83C25DA2C6A47AE08818
    }
    public static void findRoot(SinglyLinkedList firstList) throws NoSuchAlgorithmException {
        if(firstList.countNodes() % 2 != 0){    // duplicate last node for odd number of nodes
            firstList.addAtEndNode(firstList.getLast());
        }
        int count = firstList.countNodes();
        SinglyLinkedList hashFirst = new SinglyLinkedList();
        firstList.reset();
        while (firstList.hasNext()){
            hashFirst.addAtEndNode(h((String) firstList.next()));
        }
        int treeDepth = (int)Math.ceil(Math.log(count) / Math.log(2)) + 2;  // add 1 extra for the first list
        SinglyLinkedList tree = new SinglyLinkedList();
        tree.addAtEndNode(firstList);
        tree.addAtEndNode(hashFirst);
        SinglyLinkedList level = new SinglyLinkedList();
        hashFirst.reset();
        while (hashFirst.hasNext()) {
            level.addAtEndNode(hashFirst.next());
        }
            while (treeDepth - 2 > 0 && count > 1){     // continue until we find the Merkle root
                SinglyLinkedList list = new SinglyLinkedList();
            level.reset();
            while (level.current!= null && level.hasNext()){
                String concat = level.next() + (String) level.current.getData();
                level.current = level.current.getLink();
                list.addAtEndNode(h(concat));
            }
            tree.addAtEndNode(list);
            treeDepth--;
                if(list.countNodes() != 1 && list.countNodes() % 2 != 0){   // duplicate last node for odd number of nodes unless it's the Merkle root
                    list.addAtEndNode(list.getLast());
                }
            count = list.countNodes();
            level = list;
        }
            System.out.println(tree.getLast());
    }
    public static String h(String text) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i <= 31; i++) {
            byte b = hash[i];
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
