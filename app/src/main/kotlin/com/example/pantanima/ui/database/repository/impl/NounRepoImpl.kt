package com.example.pantanima.ui.database.repository.impl

import com.example.pantanima.ui.Constants
import com.example.pantanima.ui.helpers.GamePrefs
import com.example.pantanima.ui.database.dao.NounDao
import com.example.pantanima.ui.database.entity.Noun
import com.example.pantanima.ui.database.repository.NounRepo
import com.example.pantanima.ui.enums.Level
import timber.log.Timber

class NounRepoImpl(private val dao: NounDao) :
    NounRepo {

    override fun getNouns() = dao.getAll(GamePrefs.LANGUAGE, GamePrefs.ASSORTMENT_WORDS_COUNT)

    override fun insertInitialNouns() {
        val nouns = ArrayList<Noun>()
        insertLightLevelNouns(nouns)
        insertMediumLevelNouns(nouns)
        insertHardLevelNouns(nouns)
        val result = dao.insert(nouns)
        Timber.d("result = $result, nouns: $nouns")
    }

    override fun updateLastUsedTime(nouns: List<Noun>) {
        for (noun in nouns) {
            noun.lastUsedTime = System.currentTimeMillis()
        }
        dao.update(nouns)
    }

    private fun insertLightLevelNouns(list: MutableList<Noun>) {
        val armenianLightLevelList = listOf(
            "դաշտ",
            "ստեղնաշար",
            "գիր",
            "հանգիստ",
            "տրորել",
            "նախապապ",
            "թագավոր",
            "հյուսիս",
            "թաթ",
            "պահեստ",
            "գաղափար",
            "օրորոց",
            "կապ",
            "ձիարշավ",
            "հին",
            "օրհներգ",
            "պարան",
            "հորինել",
            "արագիլ",
            "հավասար",
            "ճահիճ",
            "գրավիչ",
            "կպցնել",
            "ցամաք",
            "հյուսվածք",
            "շրջանակ",
            "ճարպոտ",
            "մռայլ",
            "ռազմիկ",
            "հաջորդ",
            "խռովել",
            "անկախ",
            "նվնվալ",
            "քսել",
            "ճակատ",
            "թթվել",
            "կայծքար",
            "ճաք",
            "առյուծ",
            "շաբաթ",
            "չոբան",
            "ճերմակ",
            "կոչում",
            "հոտ",
            "ճռռոց",
            "հանդես",
            "հրաշք",
            "դատապաշտպան",
            "պարագիծ",
            "մաճառ"
        )

        for (world in armenianLightLevelList) {
            val noun = Noun(world)
            noun.level = Level.Light
            noun.language = Constants.LANGUAGE_AM
            list.add(noun)
        }
    }

    private fun insertMediumLevelNouns(list: MutableList<Noun>) {
        val armenianMediumLevelList = listOf(
            "Մեծ Հայք",
            "զիջել",
            "ճանկ",
            "փալաս",
            "լրաբեր",
            "ուղերձ",
            "բնօրինակ",
            "առատ",
            "պտտել",
            "կայուն",
            "հիստերիկ",
            "ոլորտ",
            "գայթակղել",
            "տիտան",
            "պատառ",
            "ճոխացնել",
            "մաշվել",
            "կրակոտ",
            "հարցում",
            "հետևանք",
            "դավաճան",
            "ծածկ",
            "ժամանակաշրջան",
            "նվաճում",
            "հարցաքննել",
            "այցելու",
            "այսուհետև",
            "խոնարհ",
            "հռչակագիր",
            "ընտիր",
            "մարտարվեստ",
            "թուլանալ",
            "վարկած",
            "պատանդ",
            "մասնակցել",
            "ասուլիս",
            "կտրիճ",
            "վրիժառու",
            "անկողմնակալ",
            "անգամ",
            "բաժանվել",
            "կտրուկ",
            "նյարդ",
            "վարել",
            "հիմնական",
            "գրաֆիկա",
            "ստեղծել",
            "վահանակ",
            "իշխանություն",
            "մտերիմ",
            "աղիք",
            "գլխացավ"
        )

        for (world in armenianMediumLevelList) {
            val noun = Noun(world)
            noun.level = Level.Medium
            noun.language = Constants.LANGUAGE_AM
            list.add(noun)
        }
    }

    private fun insertHardLevelNouns(list: MutableList<Noun>) {
        val armenianHardLevelList = listOf(
            "տապալում",
            "միջև",
            "սիմվոլ",
            "անցորդ",
            "գյուտ",
            "պտղաբեր",
            "թալիսման",
            "հակասություն",
            "սրտաճմլիկ",
            "ընտրյալ",
            "հոդված",
            "տխմար",
            "ծծկեր",
            "ոլորան",
            "համակարգ",
            "Արտաշեսյաններ",
            "տեխնոլոգիա",
            "Ծոփք",
            "մահապարտ",
            "իմաստ",
            "փորձագետ",
            "խայտառակ",
            "աչքալուսանք",
            "բյուրո",
            "դեմոկրատիա",
            "պայծառատես",
            "ախտանշան",
            "տաբու",
            "գրոհ",
            "հարցուփորձ",
            "պորտաբույծ",
            "հավերժական"
        )

        for (world in armenianHardLevelList) {
            val noun = Noun(world)
            noun.level = Level.Hard
            noun.language = Constants.LANGUAGE_AM
            list.add(noun)
        }
    }
}