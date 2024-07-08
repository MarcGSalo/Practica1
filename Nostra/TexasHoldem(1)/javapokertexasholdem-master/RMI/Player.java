public interface Player {

    void SetOptions(TOptions values);

    void PoketCards(TCards poketCards);

    void FlopCards(TCards flopCards);

    void TurnCard(TCards turnCard);

    void RiverCard(TCards riverCard);

    void DoBet(TInfoBet currentBet);

    void Winner(TChips pot);

    void Losser(TChips pot);

    void StatusMessage(String message);

    void EndGame(PlayerStatistics playersStats);
}