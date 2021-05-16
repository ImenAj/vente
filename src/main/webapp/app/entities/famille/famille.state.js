(function() {
    'use strict';

    angular
        .module('pfeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('famille', {
            parent: 'entity',
            url: '/famille',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.famille.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/famille/familles.html',
                    controller: 'FamilleController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('famille');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('famille-detail', {
            parent: 'famille',
            url: '/famille/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.famille.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/famille/famille-detail.html',
                    controller: 'FamilleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('famille');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Famille', function($stateParams, Famille) {
                    return Famille.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'famille',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('famille-detail.edit', {
            parent: 'famille-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/famille/famille-dialog.html',
                    controller: 'FamilleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Famille', function(Famille) {
                            return Famille.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('famille.new', {
            parent: 'famille',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/famille/famille-dialog.html',
                    controller: 'FamilleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                libelleF: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('famille', null, { reload: 'famille' });
                }, function() {
                    $state.go('famille');
                });
            }]
        })
        .state('famille.edit', {
            parent: 'famille',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/famille/famille-dialog.html',
                    controller: 'FamilleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Famille', function(Famille) {
                            return Famille.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('famille', null, { reload: 'famille' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('famille.delete', {
            parent: 'famille',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/famille/famille-delete-dialog.html',
                    controller: 'FamilleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Famille', function(Famille) {
                            return Famille.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('famille', null, { reload: 'famille' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
