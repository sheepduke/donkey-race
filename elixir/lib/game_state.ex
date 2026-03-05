defmodule GameState do
  require Feed
  # defstruct [:map, :player_positions, :players, :rules]

  @type t :: %__MODULE__{
          map: GameMap.t(),
          player_positions: map(),
          players: map(),
          rules: GameRules.t()
        }

  defstruct map: %GameMap{}, player_positions: [], players: [], rules: %GameRules{}

  def new(players, %GameMap{} = game_map, %GameRules{} = rules)
      when is_list(players) and is_struct(hd(players), Player) do
    %__MODULE__{
      map: game_map,
      player_positions: Enum.map(players, &{&1.name, 0}) |> Map.new(),
      players: Enum.map(players, &{&1.name, &1}) |> Map.new(),
      rules: rules
    }
  end

  def player_position!(%__MODULE__{player_positions: positions}, player_name) do
    Map.fetch!(positions, player_name)
  end

  def last_player?(%__MODULE__{player_positions: positions} = game_state, player_name) do
    last_position = Enum.min_by(positions, &elem(&1, 1)) |> elem(1)
    player_position!(game_state, player_name) == last_position
  end

  def forward_player!(%__MODULE__{player_positions: positions} = game_state, player_name, step) do
    new_positions = update_in(positions, [player_name], &(&1 + step))
    %__MODULE__{game_state | player_positions: new_positions}
  end

  def place_player!(%__MODULE__{player_positions: positions} = game_state, player_name, position) do
    %__MODULE__{game_state | player_positions: Map.replace!(positions, player_name, position)}
  end

  def get_player!(%__MODULE__{players: players}, player_name) do
    Map.fetch!(players, player_name)
  end

  def update_player!(%__MODULE__{} = game_state, player) do
    update_player!(game_state, player.name, fn _ -> player end)
  end

  def update_player!(%__MODULE__{players: players} = game_state, player_name, fun) do
    %__MODULE__{game_state | players: Map.update!(players, player_name, fun)}
  end

  @doc """
  Returns the winner (player name) or nil.
  """
  def get_winner(%__MODULE__{map: %GameMap{length: map_length}, player_positions: positions}) do
    winner_list =
      positions
      |> Map.filter(fn {_, position} -> position > map_length end)
      |> Map.to_list()

    case winner_list do
      [] -> nil
      [{winner, _}] -> winner
    end
  end
end
