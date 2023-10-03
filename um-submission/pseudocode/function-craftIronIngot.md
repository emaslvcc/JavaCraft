### function craftIronIngot()
{
if (inventory contains `3 x IRON_ORE`) {

remove `3 x IRON_ORE` from inventory
add `1 x IRON_INGOT` to inventory
Print "Crafted Iron Ingot." to the console

} else {

Print "Insufficient resources to craft Iron Ingot." to the console

}
}