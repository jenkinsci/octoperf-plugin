package org.jenkinsci.plugins.octoperf.result;




/**
 * Defines all the states a test result can have.
 * 
 * <p>
 * Once a state has been reached, it should never go back to a previous state.
 * </p>
 * 
 * @author jerome
 *
 */
public enum BenchResultState {
  
  /**
   * the batch has been created. It will be switched to pending once all containers have 
   * been created.
   */
  CREATED,
  /**
   * The batch has not been processed yet. It's pending to be scheduled on Rancher.
   */
  PENDING,
  /**
   * The batch has been scheduled for execution on the cluster.
   */
  SCHEDULED,

  /**
   * Machines are being started for this scenario schedule
   */
  SCALING,

  /**
   * Containers are being launched on the agents
   */
  PREPARING,

  /**
   * Tasks on the cluster are currently initializing the load generators.
   */
  INITIALIZING,

  /**
   * The load generators have joined the test and are up and running.
   */
  RUNNING {
    @Override
    public boolean isRunning() {
      return true;
    }
  },
  
  /**
   * The schedule execution is finished. There is no feedback on successful or failed execution yet.
   */
  FINISHED {
    @Override
    public boolean isTerminalState() {
      return true;
    }
  },
  
  /**
   * The schedule has been cancelled by the user.
   */
  ABORTED {
    @Override
    public boolean isTerminalState() {
      return true;
    }
  },
  /**
   * The batch could not be executed because machines could not be provisioned.
   * Happens when using on-premise machines with not enough machines.
   */
  ERROR {
    @Override
    public boolean isTerminalState() {
      return true;
    }
    
    @Override
    public boolean isError() {
      return true;
    }
  };
  
  public boolean isTerminalState() {
    return false;
  }
  
  public boolean isRunning() {
    return false;
  }
  
  public boolean isError() {
    return false;
  }
}
