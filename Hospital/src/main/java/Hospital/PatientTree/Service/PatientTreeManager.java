package Hospital.PatientTree.Service;

import org.springframework.stereotype.Component;

import Hospital.PatientTree.Entity.PatientBinaryTree;

@Component
public class PatientTreeManager {
    private final PatientBinaryTree patientTree = new PatientBinaryTree();

    public PatientBinaryTree getTree() {
        return patientTree;
    }
}
