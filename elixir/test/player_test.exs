defmodule PlayerTest do
  use ExUnit.Case

  @player Player.new("donkey", nil)

  test "pick_random_feed" do
    player = %Player{@player | available_feeds: [Feed.apple1(), Feed.apple1()]}

    {feed, new_player} = Player.pick_random_feed(player)

    assert feed == Feed.apple1()

    assert new_player == %Player{
             @player
             | available_feeds: [Feed.apple1()],
               used_feeds: [Feed.apple1()]
           }
  end

  test "replace_used_feed" do
    player = %Player{@player | used_feeds: [Feed.berry2()]}
    new_player = Player.replace_used_feed(player, Feed.berry2(), Feed.berry4())

    assert new_player == %Player{@player | used_feeds: [Feed.berry4()]}
  end
end
