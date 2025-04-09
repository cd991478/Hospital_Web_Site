package Hospital.PatientTree.Entity;

import Hospital.Patient.Entity.Patient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;




@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class PatientBinaryTree implements Serializable {

    private static final long serialVersionUID = 1L; // 직렬화 ID 추가
    @JsonProperty("root") // JSON 변환 시 필드 포함
    private PatientNode root;
    public Patient patient; 
    public PatientBinaryTree() {
        this.root = null;
    }
    
    @JsonIgnore
    public PatientNode getRoot() {
        return root;
    }

    public boolean insert(Patient newPatient) {
        if (root == null) {
            root = new PatientNode(newPatient); // 생성자 호출 시 올바르게 사용
            return true;
        }
        return insertRecursive(root, newPatient);
    }
    
    private boolean insertRecursive(PatientNode current, Patient newPatient) {
        if (newPatient.getP_Id() < current.getPatient().getP_Id()) { 
            if (current.getLeft() == null) {
                current.setLeft(new PatientNode(newPatient));
                return true;
            }
            return insertRecursive(current.getLeft(), newPatient);
        } else if (newPatient.getP_Id() > current.getPatient().getP_Id()) {
            if (current.getRight() == null) {
                current.setRight(new PatientNode(newPatient));
                return true;
            }
            return insertRecursive(current.getRight(), newPatient);
        }
        return false; 
    }
    
    public Patient search(Integer P_Id) {
        return searchRecursive(root, P_Id);
    }
    private Patient searchRecursive(PatientNode node, Integer P_Id) {
        if (node == null) return null;

        int cmp = P_Id.compareTo(node.patient.getP_Id());
        if (cmp == 0) {
            return node.patient;
        } else if (cmp < 0) {
            return searchRecursive(node.left, P_Id);
        } else {
            return searchRecursive(node.right, P_Id);
        }
    }
}
