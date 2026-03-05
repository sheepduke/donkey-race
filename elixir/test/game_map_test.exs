defmodule GameMapTest do
  use ExUnit.Case

  @game_map GameMap.short_map()

  test "on_vortex?" do
    assert GameMap.on_vortex?(@game_map, 11) == true
    assert GameMap.on_vortex?(@game_map, 10) == false
  end

  test "next_vortex" do
    assert GameMap.next_vortex(@game_map, 6) == 7
    assert GameMap.next_vortex(@game_map, 7) == 11
    assert GameMap.next_vortex(@game_map, 47) == 47
    assert GameMap.next_vortex(@game_map, 48) == 48
  end
end
