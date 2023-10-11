import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.financiamigo.CustomerFragment
import com.example.financiamigo.SupplierFragment

class PageAdapter(fm:FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 2;
    }

    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> {
                return SupplierFragment()
            }
            1 -> {
                return CustomerFragment()
            }
            else -> {
                return SupplierFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> {
                return "Proveedores"
            }
            1 -> {
                return "Clientes"
            }
        }
        return super.getPageTitle(position)
    }

}