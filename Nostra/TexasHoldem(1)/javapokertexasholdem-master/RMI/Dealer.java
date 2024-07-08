public interface Dealer {

    void SetOptions(TOptions values);
    
    void StartGame();
    
    void AddPlayer(PlayerInfo player);
    
    void RemovePlayer(TPlayerId playerId);
    
    void GetStatus(PlayerId player);
    
    void QuitGame();
}