package woogie.space.messenger.sign

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignViewModel: ViewModel() {
    // 라이브데이터는 xml 에서 관찰이 불가한듯하다.
    // 그래서 eMailEditText 이 친구로 라이브데이터의 값을 노출 시켜줌.
    // priavate val _email 로는 edittext 에 투 웨이 데이터 바인딩이 불가
    // edittext 의 스트링은 editable!

    // 외부에서 접근하지 못하게 private 생성
    // _eMail 의 값을 단순히 노출만 시켜줌.
    // _eMail 은 private 이지만, 믿을 수 있는 함수를 통해서 private 인 _eMail 의 값을 변경 가능.
    // 이로서 view 확실하게 데이터만 보여주는 역할을 수행한다.

    val eMail =  MutableLiveData<String>()

    val eMailButtonEnable =  MutableLiveData<Boolean>()
    private val _eMailValidity = MutableLiveData<Boolean>()

    val emailValidity : LiveData<Boolean>
        get() = _eMailValidity

    fun onChangedEmailBoolean() {
        _eMailValidity.value = eMailButtonEnable.value
    }

    val passWord =  MutableLiveData<String>()
    val passWordButtonEnable =  MutableLiveData<Boolean>()

    val name =  MutableLiveData<String>()
    val nameButtonEnable =  MutableLiveData<Boolean>()

    fun Navigator_Sign_OR_Login() {

    }

    fun Navigator_NameFragment() {
    }

    fun SignUP() {

    }
}