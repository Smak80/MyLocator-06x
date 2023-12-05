package ru.smak.mylocator

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import ru.smak.mylocator.ui.theme.MyLocatorTheme
import ru.smak.mylocator.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {

    private val mvm: MainViewModel by lazy{
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(ACCESS_FINE_LOCATION, false) -> {
                mvm.showRequestDialog = false
            }
            permissions.getOrDefault(ACCESS_COARSE_LOCATION, false) -> {
                mvm.showRequestDialog = false
            }
            else -> {
                mvm.showRequestDialog = false
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyLocatorTheme {
                MainUI(
                    mvm,
                    Modifier.fillMaxSize()
                )
                mvm.showRequestDialog =
                    !isPermissionsGranted(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION, context = this)
                if (mvm.showRequestDialog){
                    LocationRequestDialog(
                        onDeny = {
                            finish()
                        }
                    ){
                        // Формирование запроса из системы на доступ к геолокации
                        mvm.showRequestDialog = false
                        locationPermissionRequest.launch(
                            arrayOf(
                                ACCESS_FINE_LOCATION,
                                ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                }
            }
        }
    }

    fun isPermissionsGranted(vararg permissions: String, context: Context) =
        permissions.fold(true) { acc, perm ->
            acc && context.checkSelfPermission(perm) == PackageManager.PERMISSION_GRANTED
        }
}

@Composable
fun MainUI(
    mvm: MainViewModel,
    modifier: Modifier = Modifier,
){
    Text(
        text = mvm.location,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationRequestDialog(
    modifier: Modifier = Modifier,
    onDeny: ()->Unit,
    onAllow: ()->Unit,
){
    AlertDialog(
        onDismissRequest = { onDeny() },
    ) {
        ElevatedCard(
            modifier = modifier.shadow(3.dp, shape = RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painterResource(id = R.drawable.twotone_not_listed_location_48),
                    contentDescription = null,
                    tint = colorResource(id = R.color.brown)
                )
                Text(stringResource(R.string.loc_permission_request))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = { onDeny() }) {
                        Text("No")
                    }
                    Button(onClick = { onAllow() }) {
                        Text("Yes")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun LocationRequestDialogPreview(){
    LocationRequestDialog(onDeny = { /*TODO*/ }) {

    }
}