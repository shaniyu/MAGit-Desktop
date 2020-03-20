package magitUI;

import javafx.concurrent.Task;
import magitEngine.TaskValue;
import magitObjects.Repository;
import xmlObjects.MagitRepository;

public class LoadRepoFromXmlTask extends Task<TaskValue> {

    private TaskValue taskValue;
    private String xmlFilePath;
    private Repository repository;
    private MagitRepository magitRepository;

    public LoadRepoFromXmlTask(String pathToXmlFile, Repository repo, MagitRepository magitRepository)
    {
        repository = repo;
        this.xmlFilePath = pathToXmlFile;
        this.magitRepository = magitRepository;
        taskValue = new TaskValue();
    }

    @Override
    protected TaskValue call()
    {
        int taskPrecentage = 0;
        int maxPrecentage = 100;

        updateMessage("Spreading xml file to a repository");
        try{

            updateProgress(taskPrecentage, maxPrecentage);

            // instead of calling spreadRepositoryFromXml, we call each step, and promote the progress label and progressbar
            updateMessage("Creating empty repository..");
            //Creating empty repository in this location
            repository.initializeEmptyRepository(magitRepository.getLocation(), magitRepository.getName());
            taskPrecentage += 20; // 20% for create empty repo
            updateProgress(taskPrecentage, maxPrecentage);


            if (repository.isRemoteMagitRepo(magitRepository))
            {
                // it means the repo that represented in xml, is a clone of a remote repo
                repository.initializeRemoteFileAndRemotePath(magitRepository, magitRepository.getMagitRemoteReference().getName());
            }
            //Spreading all objects files and branches files- and then loading the repository to the system as if
            //we're loading an existing repository (option 3 in the menu)

            updateMessage("Creating magit objects..");
            repository.spreadRepositoryFromXmlToObjects(magitRepository);
            //Adding previous commits can be done only after calculating all commit sha1s, so we add them after spreading all files in objects
            taskPrecentage += 20;
            updateProgress(taskPrecentage, maxPrecentage);

            repository.addPreviousCommitToEachCommitFile(magitRepository.getMagitCommits());
            taskPrecentage += 20;
            updateProgress(taskPrecentage, maxPrecentage);

            repository.spreadRepositoryFromXmlToBranches(magitRepository);
            taskPrecentage += 20;
            updateProgress(taskPrecentage, maxPrecentage);

            repository.loadRepositoryFromPath(magitRepository.getLocation());
            taskPrecentage += 10;
            updateProgress(taskPrecentage, maxPrecentage);

            updateMessage("Put repository on file system..");
            //In case we loaded an empty repository- the headBranch commit is null so we don't want to spread the working copy
            if(!(repository.getHeadBranch().getCommit() == null)){
                repository.putCommitContentOnWorkingCopy(repository.getHeadBranch().getCommit().getSha1());
            }
            taskPrecentage += 10;
            updateProgress(taskPrecentage, maxPrecentage);

            // do stuff here - load repo from xml
            updateMessage("Done creating repository from xml file!");
            taskValue.setTaskSuccess(true);
            taskValue.setException(null);
            return taskValue;
        }
        catch (Exception e){
            taskValue.setException(e);
            taskValue.setTaskSuccess(false);
            return taskValue;
        }
    }


}