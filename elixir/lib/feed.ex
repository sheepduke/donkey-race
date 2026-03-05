defmodule Feed do
  @type t :: %__MODULE__{
          name: binary(),
          type: atom(),
          value: integer(),
          price: integer()
        }

  defstruct [:name, :type, :value, :price]

  @types [:apple, :corn, :berry, :cabbage, :prune, :carrot, :radish, :grass]

  defguard is_feed(term) when is_struct(term, __MODULE__)

  defguard is_valid_type(term)
           when is_atom(term) and term in @types

  def new(name, type, value, price)
      when is_binary(name) and is_valid_type(type) and
             is_integer(value) and price >= 0 and
             is_integer(price) and value >= 0 do
    %__MODULE__{name: name, type: type, value: value, price: price}
  end

  def grass(), do: new("grass", :grass, 0, 0)

  def apple1(), do: new("apple1", :apple, 1, 1)
  def apple2(), do: new("apple2", :apple, 2, 2)
  def apple4(), do: new("apple4", :apple, 4, 3)

  def corn1(), do: new("corn1", :corn, 1, 1)
  def corn2(), do: new("corn2", :corn, 2, 2)
  def corn4(), do: new("corn4", :corn, 4, 3)

  def berry1(), do: new("berry1", :berry, 1, 1)
  def berry2(), do: new("berry2", :berry, 2, 2)
  def berry4(), do: new("berry4", :berry, 4, 3)

  def cabbage1(), do: new("cabbage1", :cabbage, 1, 1)
  def cabbage2(), do: new("cabbage2", :cabbage, 2, 2)
  def cabbage4(), do: new("cabbage4", :cabbage, 4, 3)

  def prune(), do: new("prune", :prune, 5, 4)
  def carrot(), do: new("carrot", :carrot, 5, 4)

  def radish(), do: new("radish", :radish, 8, 5)
end
