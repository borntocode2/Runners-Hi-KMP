package good.space.runnershi.user.domain

import good.space.runnershi.user.UserType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType
import jakarta.persistence.Table

@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name = "users")
abstract class User(
    var name: String,

    @Column(unique = true, nullable = false)
    var email: String,

    @Enumerated(EnumType.STRING)
    var userType: UserType,
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    var exp: Long = 0;

    fun increaseExp(amount: Long) {
        this.exp += amount
    }

}

