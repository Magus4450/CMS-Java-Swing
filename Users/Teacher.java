package Users;

import java.util.ArrayList;

public class Teacher extends User{
    private ArrayList<Module> moduleList;

    public ArrayList<Module> getModuleList() {
        return moduleList;
    }
    public void setModuleList(ArrayList<Module> moduleList) {
        this.moduleList = moduleList;
    }


}
